package ru.yandex.saver12.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import ru.yandex.saver12.dao.impl.PhotoDAOImpl;
import ru.yandex.saver12.dao.impl.UserDAOImpl;
import ru.yandex.saver12.model.Photo;
import ru.yandex.saver12.model.User;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

public class PhotoDAOTest extends SetUpTest {

    private static PhotoDAO photoDAO = new PhotoDAOImpl();
    private static UserDAO userDAO = new UserDAOImpl();

    @Override
    protected IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("photo-data.xml"));
    }

    @Test
    public void testGetPhotosList() throws Exception {
        List<Photo> list = photoDAO.getPhotosList(2);
        assertThat(list.get(0).getId(), is(1));
    }

    @Test
    public void testCreatePhoto() throws Exception {
        Photo photo = photoDAO.createPhoto("New", 2);
        assertThat(photo.getId(), is(greaterThan(0)));
        assertThat(photo.getName(), is("New"));
    }

    @Test
    public void testDeletePhoto() throws Exception {
        photoDAO.deletePhoto(1);
        changeConnection();
        List<Photo> list = photoDAO.getPhotosList(2);
        assertThat(list.isEmpty(), is(true));
    }

    @Test
    public void testUpdateDescription() throws Exception {
        photoDAO.updateDescription(1, "Okay");
        changeConnection();
        List<Photo> list = photoDAO.getPhotosList(2);
        assertThat(list.get(0).getName(), is("Okay"));
    }

    @Test
    public void testChangeProfilePhoto() throws Exception {
        photoDAO.changeProfilePhoto(2, 1);
        changeConnection();
        User user = userDAO.getUser("bunny@mail.ru", "qwerty");
        assertThat(user.getPhotoId(), is(1));
    }
}