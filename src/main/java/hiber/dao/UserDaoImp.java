package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private SessionFactory sessionFactory;

    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    @Override
    public void deleteAllUsers() {
        List<User> users = listUsers();
        for (User user : users) {
            sessionFactory.getCurrentSession().delete(user);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public User findOwner(String carModel, String carSeries) {
        TypedQuery<Car> carQuery = sessionFactory.getCurrentSession().createQuery("from Car where model = :car_name and series = :car_series")
                .setParameter("car_name", carModel)
                .setParameter("car_series", carSeries);
        List<Car> carList = carQuery.getResultList();
        User userFind = new User();
        if (!carList.isEmpty()) {
            Car carFind = carList.get(0);
            List<User> userList = listUsers();
            userFind = userList.stream()
                    .filter(user -> user.getCar().equals(carFind))
                    .findAny()
                    .orElse(null);
            return userFind;
        }
        return userFind;
    }
}
