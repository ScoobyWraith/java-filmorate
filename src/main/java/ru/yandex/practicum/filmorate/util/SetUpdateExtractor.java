package ru.yandex.practicum.filmorate.util;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class SetUpdateExtractor<T> {
    private final Set<T> toRemove;
    private final Set<T> toAdd;

    public SetUpdateExtractor(Collection<T> currentSet, Collection<T> newSet) {
        toRemove = new HashSet<>(currentSet);
        toRemove.removeAll(newSet);

        toAdd = new HashSet<>(currentSet);
        toAdd.removeAll(newSet);
    }
}
