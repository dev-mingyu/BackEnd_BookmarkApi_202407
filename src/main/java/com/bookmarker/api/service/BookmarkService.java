package com.bookmarker.api.service;

import com.bookmarker.api.domain.Bookmark;
import com.bookmarker.api.domain.BookmarkRepository;
import com.bookmarker.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository repository;
    private final BookmarkMapper mapper;

    @Transactional(readOnly = true)
    public BookmarksDTO<?> getBookmarks(Pageable pageable) {
        Page<BookmarkDTO> bookmarks = repository.findBookmarks(pageable);

        return new BookmarksDTO<>(bookmarks);
    }

    @Transactional(readOnly = true)
    public BookmarksDTO<?> searchBookmarks(String query, Pageable pageable) {

//        Page<BookmarkDTO> bookmarkDTOS = repository.searchBookmarks(query, pageable);
//        Page<BookmarkDTO> bookmarkDTOS = repository.findByTitleContainingIgnoreCase(query, pageable);
        Page<BookmarkVM> bookmarkDTOS = repository.findByTitleContainsIgnoreCase(query, pageable);

        return new BookmarksDTO<>(bookmarkDTOS);
    }

    public BookmarkDTO createBookmark(CreateBookmarkRequest request) {
        Bookmark bookmark = new Bookmark(request.getTitle(), request.getUrl(), Instant.now());
        Bookmark savedBookmark = repository.save(bookmark);
        return mapper.toDTO(savedBookmark);
    }
}
