
package com.example;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@Startup
@LocalBean
public class Movies {

    @PersistenceContext(unitName = "movie-unit"
    // ,
    // type = PersistenceContextType.EXTENDED
    )
    private EntityManager entityManager;

    public void addMovie(Movie movie) throws Exception {
        entityManager.persist(movie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        entityManager.remove(movie);
    }

    public List<Movie> getMovies() throws Exception {
        Query query = entityManager.createQuery("SELECT m from Movie as m");
        return query.getResultList();
    }

    @PostConstruct
    public void testHibernate() throws Exception {
        System.out.println("##### testHibernate() ");
        addMovie(new Movie("Quentin Tarantino", "Reservoir Dogs", 1992));
        addMovie(new Movie("Joel Coen", "Fargo", 1996));
        addMovie(new Movie("Joel Coen", "The Big Lebowski", 1998));

        List<Movie> list = getMovies();
        System.out.println(" ##### List.size() " + list.size());

        for (Movie movie : list) {
            System.out.println(" ##### movie " + movie.getTitle());
            deleteMovie(movie);
        }
        System.out.println(" ##### Movies.getMovies() " + getMovies().size());
    }
}
