package ru.student.notemanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testNoteConstructorAndGetters() {
        Note note = new Note(1L, "Заголовок", "Содержание");

        assertEquals(1L, note.getId());
        assertEquals("Заголовок", note.getTitle());
        assertEquals("Содержание", note.getContent());
    }

    @Test
    void testNoteSetters() {
        Note note = new Note();
        note.setId(2L);
        note.setTitle("Новый заголовок");
        note.setContent("Новое содержание");

        assertEquals(2L, note.getId());
        assertEquals("Новый заголовок", note.getTitle());
        assertEquals("Новое содержание", note.getContent());
    }

    @Test
    void testDefaultConstructor() {
        Note note = new Note();
        assertNotNull(note);
        assertNull(note.getId());
        assertNull(note.getTitle());
        assertNull(note.getContent());
    }
}