package ch.hftm;

import ch.hftm.dtos.CommentNewDto;
import ch.hftm.dtos.EntryDto;
import ch.hftm.dtos.EntryNewDto;
import ch.hftm.dtos.EntryOverviewDto;
import ch.hftm.entities.Comment;
import ch.hftm.entities.Entry;
import graphql.GraphQLException;
import io.quarkus.logging.Log;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.graphql.*;

import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GraphQLApi
public class BlogResource {

    @Query("entries")
    @Description("get all blog entries")
    public List<EntryOverviewDto> getEntries(@DefaultValue("1") Integer from, @DefaultValue("50") Integer to) {
        List<Entry> entryList = Entry.listAll();

        if (from != null && to != null) {
            if (from <= 0) {
                throw new GraphQLException("from must be greater than 0");
            }
            entryList = entryList.stream().skip(from-1).limit(to-from+1).collect(Collectors.toList());
        }

        return entryList.stream().map(e -> new EntryOverviewDto(e, "user" /*identity.getPrincipal().getName() */)).collect(Collectors.toList());
    }

    @Query("entry")
    public EntryDto getEntry(long id) {
        var actualUser =  "user" /* identity.getPrincipal().getName() */;
        Optional<Entry> entryOptional = Entry.findByIdOptional(id);
        if (entryOptional.isEmpty()) {
            throw new GraphQLException("not found");
        }
        return new EntryDto(entryOptional.get(), actualUser);
    }

    @Mutation("changeLike")
    @Transactional
    public boolean changeLike(long id, boolean like) {
        var actualUser = "user" /* identity.getPrincipal().getName() */;
        Optional<Entry> entryOptional = Entry.findByIdOptional(id);
        if (entryOptional.isEmpty()) {
            throw new GraphQLException("Entry with this id is not available!");
        }
        if (like) {
            entryOptional.get().userIdLikes.add(actualUser);
        } else {
            entryOptional.get().userIdLikes.remove(actualUser);
        }
        return true;
    }

    @Mutation("addComment")
    @Transactional
    public boolean addComment(long id, @NotNull CommentNewDto input) {
        var actualUser = "user" /* identity.getPrincipal().getName() */;
        Optional<Entry> entryOptional = Entry.findByIdOptional(id);
        if (entryOptional.isEmpty()) {
            throw new GraphQLException("Entry with this id is not available!");
        }
        var comment = Comment.builder().comment(input.comment()).userId(actualUser).build();
        entryOptional.get().comments.add(comment);
        return true;
    }

    @Mutation("addEntry")
    @Transactional
    public boolean addEntry(@NotNull EntryNewDto input) {
        var actualUser = "user" /* identity.getPrincipal().getName() */;
        persistEntryFromDto(input, actualUser);
        return true;
    }

    @Mutation("deleteEntry")
    @Transactional
    public boolean deleteEntry(long id) {
        Optional<Entry> entryOptional = Entry.findByIdOptional(id);
        if (entryOptional.isEmpty()) {
            throw new GraphQLException("Entry with this id is not available!");
        }
        var actualUser = "user" /* identity.getPrincipal().getName() */;
        if (actualUser.equals(entryOptional.get().autor) /* || identity.getRoles().contains("admin") */) {
            entryOptional.get().delete();
        } else {
            throw new GraphQLException("You are only allowed to delete your own posts!");
        }
        return true;
    }

    @Mutation("replaceEntry")
    @Transactional
    public boolean replaceEntry(long id, @NotNull EntryNewDto input) {
        Optional<Entry> originalEntryOptional = Entry.findByIdOptional(id);
        if (originalEntryOptional.isEmpty()) {
            throw new GraphQLException("Entry with this id is not available!");
        }

        var actualUser = "user" /* identity.getPrincipal().getName() */;
        if (actualUser.equals(originalEntryOptional.get().autor)
                /* || identity.getRoles().contains("admin") */) {
            var originalEntry = originalEntryOptional.get();
            if (input.content() != null && !input.content().isEmpty()) {
                originalEntry.content = input.content();
            }
            if (input.title() != null && !input.title().isEmpty()) {
                originalEntry.title = input.title();
            }
        } else {
            throw new GraphQLException("You are only allowed to replace  your own posts!");
        }
        return true;
    }

    @Transactional
    Entry persistEntryFromDto(EntryNewDto entryDto, String actualUser) {
        var entry = Entry.builder().content(entryDto.content()).title(entryDto.title())
                .autor(actualUser).build();
        entry.persist();
        return entry;
    }
}