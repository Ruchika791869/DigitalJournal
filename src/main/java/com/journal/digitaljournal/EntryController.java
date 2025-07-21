package com.journal.digitaljournal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class EntryController {

    @Autowired
    private JournalEntryRepository entryRepository;

    // Homepage
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // New Entry Form
    @GetMapping("/new-entry")
    public String newEntryForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Optional<JournalEntry> entry = entryRepository.findById(id);
            entry.ifPresent(value -> model.addAttribute("entry", value));
            model.addAttribute("isEdit", true);
        } else {
            model.addAttribute("entry", new JournalEntry());
            model.addAttribute("isEdit", false);
        }
        return "new-entry";
    }

    // Save Entry (Create or Update)
    @PostMapping("/save-entry")
    public String saveEntry(@ModelAttribute JournalEntry entry) {
        entry.setDate(LocalDate.now());
        entryRepository.save(entry);
        return "redirect:/entries";
    }

    // View All Entries (Optionally filtered by category or date)
    @GetMapping("/entries")
    public String viewEntries(@RequestParam(required = false) String category,
                              @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {
        List<JournalEntry> entries;

        if (category != null && !category.isEmpty() && date != null) {
            entries = entryRepository.findByCategoryAndDate(category, date);
        } else if (category != null && !category.isEmpty()) {
            entries = entryRepository.findByCategory(category);
        } else if (date != null) {
            entries = entryRepository.findByDate(date);
        } else {
            entries = entryRepository.findAll();
        }

        model.addAttribute("entries", entries);
        return "entries";
    }

    // Delete Entry (POST route matching HTML)
    @PostMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id) {
        entryRepository.deleteById(id);
        return "redirect:/entries";
    }

    // Calendar View
    @GetMapping("/calendar")
    public String calendarView(Model model) {
        List<JournalEntry> entries = entryRepository.findAll();
        model.addAttribute("entries", entries);
        return "calendar";
    }
}
