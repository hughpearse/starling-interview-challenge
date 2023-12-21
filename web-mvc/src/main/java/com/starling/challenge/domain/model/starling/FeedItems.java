package com.starling.challenge.domain.model.starling;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper around multiple feed items
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedItems {
    private List<FeedItem> feedItems;
}
