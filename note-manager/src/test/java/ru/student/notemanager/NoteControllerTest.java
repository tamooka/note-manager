package ru.student.notemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Note testNote;

    @BeforeEach
    void setUp() {
        try {
            ResponseEntity<List<Note>> getAllResponse = restTemplate.exchange(
                    "/notes",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Note>>() {}
            );
            if (getAllResponse.getBody() != null) {
                for (Note note : getAllResponse.getBody()) {
                    restTemplate.delete("/notes/" + note.getId());
                }
            }
        } catch (Exception e) {
        }
        testNote = new Note(1L, "Тестовая заметка", "Тестовое содержание");
    }

    @Test
    void testGetNotes_ShouldReturnEmptyListInitially() {
        ResponseEntity<List> response = restTemplate.getForEntity("/notes", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testAddNote_ShouldCreateNote() {
        ResponseEntity<Note> response = restTemplate.postForEntity("/notes", testNote, Note.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Тестовая заметка", response.getBody().getTitle());
        assertEquals("Тестовое содержание", response.getBody().getContent());
    }

    @Test
    void testGetNoteById_ShouldReturnNote() {
        restTemplate.postForEntity("/notes", testNote, Note.class);

        ResponseEntity<Note> response = restTemplate.getForEntity("/notes/1", Note.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetNoteById_ShouldReturnNullForNotFound() {
        ResponseEntity<Note> response = restTemplate.getForEntity("/notes/999", Note.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateNote_ShouldUpdateExistingNote() {
        restTemplate.postForEntity("/notes", testNote, Note.class);

        Note updatedNote = new Note(1L, "Обновлённый заголовок", "Обновлённое содержание");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Note> requestEntity = new HttpEntity<>(updatedNote, headers);

        ResponseEntity<Note> response = restTemplate.exchange(
                "/notes/1",
                HttpMethod.PUT,
                requestEntity,
                Note.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Обновлённый заголовок", response.getBody().getTitle());
        assertEquals("Обновлённое содержание", response.getBody().getContent());
    }
    @Test
    void testDeleteNote_ShouldRemoveNote() {
        restTemplate.postForEntity("/notes", testNote, Note.class);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/notes/1",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Заметка удалена", deleteResponse.getBody());

        ResponseEntity<Note> getResponse = restTemplate.getForEntity("/notes/1", Note.class);
        assertNull(getResponse.getBody());
    }
}