package org.development1;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.development1.entity.Book;

import javax.sql.DataSource;
import javax.ws.rs.core.Application;

public class LibraryApplication extends Application<LibraryConfiguration> {

    private final HibernateBundle<LibraryConfiguration> hibernateBundle=
            new HibernateBundle<>(Book.class){
                @Override
                public DataSourceFactory getDataSourceFactory (LibraryConfiguration configuration){
                    return configuration.getDatabase();
                }
            }


    public static void main(String[] args) {
        new LibraryApplication().run(args);

    @Override
    public void initialize(Bootstrap<LibraryConfiguration>bootstrap)
        {
            //logic
        }

    @Override
    public void run(LibraryConfiguration configuration, Environment environment)
        {
            System.out.println("Hello, Dropwizard!");
        }

    }
}
