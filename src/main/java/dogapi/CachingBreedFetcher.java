package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.delegate = Objects.requireNonNull(fetcher, "fetcher must not be null");
    }


    @Override
    public List<String> getSubBreeds(String breed) {
        String key = (breed == null) ? null : breed.trim().toLowerCase();

        if (cache.containsKey(key)) {
            return new ArrayList<>(cache.get(key));
        }

        callsMade++;
        List<String> result = delegate.getSubBreeds(breed);
        List<String> toCache = new ArrayList<>(result);
        cache.put(key, toCache);
        return new ArrayList<>(toCache);
    }

    public int getCallsMade() {
        return callsMade;
    }
}