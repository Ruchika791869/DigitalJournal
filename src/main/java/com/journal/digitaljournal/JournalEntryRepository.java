package com.journal.digitaljournal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByCategory(String category);
    List<JournalEntry> findByDate(LocalDate date);
	List<JournalEntry> findByCategoryAndDate(String category, LocalDate date);
}
