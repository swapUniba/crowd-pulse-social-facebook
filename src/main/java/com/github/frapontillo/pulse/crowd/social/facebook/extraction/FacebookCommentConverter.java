package com.github.frapontillo.pulse.crowd.social.facebook.extraction;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.social.extraction.ExtractionParameters;
import com.github.frapontillo.pulse.crowd.social.extraction.MessageConverter;
import facebook4j.Comment;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Francesco Pontillo
 */
public class FacebookCommentConverter extends MessageConverter<Comment> {

    public FacebookCommentConverter(ExtractionParameters parameters) {
        super(parameters);
    }

    @Override
    public Message fromSpecificExtractor(Comment original, HashMap<String, Object> additionalData) {
        Message message = new Message();
        message.setoId(original.getId());
        message.setText(original.getMessage());
        message.setFromUser(original.getFrom().getId());

        // if the current message is a comment to another message, set its parent here
        if (additionalData != null) {
            String parent = (String) additionalData.get(DATA_REPLY_TO_COMMENT);
            if (parent != null) {
                message.setParent(parent);
            }
            message.setToUsers(Arrays.asList((String) additionalData.get(DATA_REPLY_TO_USER)));
        }

        message.setDate(original.getCreatedTime());

        // TODO: implement proper likes.summary(true) and likes.summary.total_count fetching
        message.setFavs(original.getLikeCount());

        return message;
    }
}
