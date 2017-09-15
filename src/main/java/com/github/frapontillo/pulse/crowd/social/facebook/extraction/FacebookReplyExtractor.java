package com.github.frapontillo.pulse.crowd.social.facebook.extraction;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.social.extraction.ExtractionParameters;
import com.github.frapontillo.pulse.crowd.social.extraction.IReplyExtractor;
import com.github.frapontillo.pulse.crowd.social.facebook.FacebookFactory;
import facebook4j.Comment;
import facebook4j.FacebookException;
import facebook4j.Reading;
import facebook4j.ResponseList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Francesco Pontillo
 */
public class FacebookReplyExtractor extends IReplyExtractor {
    public final static String PLUGIN_NAME = "reply-extractor-facebook";

    @Override public List<Message> getReplies(Message message, ExtractionParameters parameters) {
        Reading reading = new Reading().summary().filter("stream");
        FacebookCommentConverter converter = new FacebookCommentConverter(parameters);
        HashMap<String, Object> map = new HashMap<>();
        map.put(FacebookCommentConverter.DATA_REPLY_TO_COMMENT, message.getoId());
        map.put(FacebookCommentConverter.DATA_REPLY_TO_USER, message.getFromUser());
        map.put(FacebookCommentConverter.DATA_SOURCE, parameters.getSource());
        List<Message> messages = new ArrayList<>();
        String cursor = null;

        try {
            do {
                if (cursor != null) {
                    reading.after(cursor);
                }
                ResponseList<Comment> comments = FacebookFactory.getFacebookInstance().posts()
                        .getPostComments(message.getoId(), reading);
                converter.addFromExtractor(comments, messages, map);
                if (comments.getPaging() == null) {
                    cursor = null;
                } else {
                    cursor = comments.getPaging().getCursors().getAfter();
                }
            } while (cursor != null);
        } catch (FacebookException ignored) {
        }

        return messages;
    }

    @Override public String getName() {
        return PLUGIN_NAME;
    }
}
