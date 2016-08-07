package ru.yandex.saver12.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import ru.yandex.saver12.dao.impl.AudioDAOImpl;
import ru.yandex.saver12.model.Audio;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

public class AudioDAOTest extends SetUpTest {

    private static AudioDAO audioDAO = new AudioDAOImpl();

    @Override
    protected IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("audio-data.xml"));
    }

    @Test
    public void testGetAudiosList() throws Exception {
        List<Audio> list = audioDAO.getAudiosList(2);
        assertThat(list.get(0).getId(), is(1));
    }

    @Test
    public void testCreateAudio() throws Exception {
        Audio audio = audioDAO.createAudio("New", 2);
        assertThat(audio.getId(), is(greaterThan(0)));
        assertThat(audio.getName(), is("New"));
    }

    @Test
    public void testDeleteAudio() throws Exception {
        audioDAO.deleteAudio(1);
        changeConnection();
        List<Audio> list = audioDAO.getAudiosList(2);
        assertThat(list.isEmpty(), is(true));
    }

    @Test
    public void testUpdateDescription() throws Exception {
        audioDAO.updateDescription(1, "Okay");
        changeConnection();
        List<Audio> list = audioDAO.getAudiosList(2);
        assertThat(list.get(0).getName(), is("Okay"));
    }
}