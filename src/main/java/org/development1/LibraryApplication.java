package org.development1;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.development1.entity.Book;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.core.Application;
import java.util.logging.Logger;

public class LibraryApplication extends Application<LibraryConfiguration> {

    private static final Logger LOGGER= LoggerFactory.getLogger(LibraryApplication.class);

    public static void main(String[] args) {
        new LibraryApplication().run(args);
    }

    @Override
    public String getName() {
        return "LibraryManagement";
    }


    @Override
    public void initialize(Bootstrap<LibraryConfiguration>bootstrap)
        {
            //logic
        }

    @Override
    public void run(LibraryConfiguration configuration, Environment environment)
        {
            System.out.println("Hello, Dropwizard!");

            final SessionFactory sessionFactory= buildSessionFactory(configuration.getDatabase());
            final MetricRegistry metricRegistry=environment.metrics();

            final BookDAO bookDAO = new BookDAO(SessionFactory);

            final BookResource bookResource=new BookResource(bookDAO, metricRegistry);

            environment.jersey().register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(SessionFactory).to(SessionFactory.class);
                    bind(bookDAO).to(BookDAO.class).in(Singleton.class);
                    bind(bookResource).to(BookResource.class).in(Singleton.class);
                }
            });
        }

        environment.jersey().register(bookResource);
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        enenvironment.healthChecks().register("database", new DatabaseHealthCheck(SessionFactory));

        Logger.info("Library Management Application startted successfully");

    }
