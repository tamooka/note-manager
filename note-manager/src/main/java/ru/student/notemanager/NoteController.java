package ru.student.notemanager;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final List<Note> notes = new ArrayList<>();

    @GetMapping
    public List<Note> getAll() { return notes; }

    @GetMapping("/{id}")
    public Note getById(@PathVariable Long id) {
        return notes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public Note add(@RequestBody Note note) {
        notes.add(note);
        return note;
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable Long id, @RequestBody Note updated) {
        for (Note note : notes) {
            if (note.getId().equals(id)) {
                note.setTitle(updated.getTitle());
                note.setContent(updated.getContent());
                return note;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        notes.removeIf(n -> n.getId().equals(id));
        return "Заметка удалена";
    }
}