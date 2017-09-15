package com.github.frapontillo.pulse.crowd.social.facebook.extraction;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.social.extraction.ExtractionParameters;
import com.github.frapontillo.pulse.crowd.social.extraction.MessageConverter;
import facebook4j.IdNameEntity;
import facebook4j.Post;
import facebook4j.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Francesco Pontillo
 */
public class FacebookMessageConverter extends MessageConverter<Post> {

    public FacebookMessageConverter(ExtractionParameters parameters) {
        super(parameters);
    }

    @Override
    public Message fromSpecificExtractor(Post original, HashMap<String, Object> additionalData) {
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
        }

        // the "to" user is, by convention, the first user in the "to" list
        List<String> toIds = new ArrayList<>(original.getTo().size());
        for (IdNameEntity to : original.getTo()) {
            toIds.add(to.getId());
        }
        message.setToUsers(toIds);

        // the creation time is (strangely) not always present, use the updated time info instead
        if (original.getCreatedTime() != null) {
            message.setDate(original.getCreatedTime());
        } else {
            message.setDate(original.getUpdatedTime());
        }

        // convert the referenced users
        List<String> refUsers = new ArrayList<>(original.getMessageTags().size());
        refUsers.addAll(
                original.getMessageTags().stream().map(Tag::getId).collect(Collectors.toList()));
        message.setRefUsers(refUsers);

        // TODO: implement proper likes.summary(true) and likes.summary.total_count fetching
        message.setFavs(original.getLikes().size());
        message.setShares(original.getSharesCount());

        return message;
    }
}
