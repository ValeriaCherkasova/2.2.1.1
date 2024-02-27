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
    public User findOwner(String car_model, String car_series) {
        TypedQuery<Car> findCarQuery = sessionFactory.getCurrentSession().createQuery("from Car where model = :car_name and series = :car_series")
                .setParameter("car_name", car_model)
                .setParameter("car_series", car_series);
        List<Car> findCarList = findCarQuery.getResultList();
        User FindUser = new User();
        if (!findCarList.isEmpty()) {
            Car findCar = findCarList.get(0);
            List<User> ListUser = listUsers();
            FindUser = ListUser.stream()
                    .filter(user -> user.getCar().equals(findCar))
                    .findAny()
                    .orElse(null);
            return FindUser;
        }
        return FindUser;
    }
}
