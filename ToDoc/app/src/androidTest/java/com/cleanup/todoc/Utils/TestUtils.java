package com.cleanup.todoc.Utils;


import com.cleanup.todoc.RecyclerViewMatcher;
/**
 * Class TestUtils, use to act on recyclerView in tests
 */
public class TestUtils {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }

}