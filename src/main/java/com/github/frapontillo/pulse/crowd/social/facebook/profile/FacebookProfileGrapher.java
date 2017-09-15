package com.github.frapontillo.pulse.crowd.social.facebook.profile;

import com.github.frapontillo.pulse.crowd.data.entity.Profile;
import com.github.frapontillo.pulse.crowd.social.facebook.FacebookFactory;
import com.github.frapontillo.pulse.crowd.social.profile.IProfileGrapher;
import com.github.frapontillo.pulse.crowd.social.profile.ProfileParameters;
import facebook4j.FacebookException;
import facebook4j.Friend;
import facebook4j.Reading;
import facebook4j.ResponseList;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile graph builder for Facebook API.
 * <p/>
 * Note: as of now, building the connection graph does not work for two reasons:
 * <ol>
 * <li>a user's friends can be fetched only if the user has granted permission to the app</li>
 * <li>a page's followers cannot be fetched at all</li>
 * </ol>
 * This graph builder has therefore been implemented as a sample.
 *
 * @author Francesco Pontillo
 */
public class FacebookProfileGrapher extends IProfileGrapher {
    public final static String PLUGIN_NAME = "facebook-profile-grapher";

    @Override public List<Profile> getConnections(Profile profile, ProfileParameters parameters) {
        FacebookProfileConverter converter = new FacebookProfileConverter(parameters);
        List<Profile> profiles = new ArrayList<>();
        ResponseList<Friend> friends;
        Reading reading = null;
        String cursor;
        try {
            do {
                friends = FacebookFactory.getFacebookInstance()
                        .getFriends(profile.getUsername(), reading);
                converter.addFromExtractor(friends, profiles);
                if ((cursor = friends.getPaging().getCursors().getAfter()) != null) {
                    reading = new Reading().after(cursor);
                }
            } while (cursor != null);
        } catch (FacebookException ignored) {
        }
        return profiles;
    }

    @Override public String getName() {
        return PLUGIN_NAME;
    }
}
