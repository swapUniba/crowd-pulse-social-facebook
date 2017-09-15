package com.github.frapontillo.pulse.crowd.social.facebook.profile;

import com.github.frapontillo.pulse.crowd.data.entity.Profile;
import com.github.frapontillo.pulse.crowd.social.profile.IProfiler;
import com.github.frapontillo.pulse.crowd.social.profile.ProfileParameters;
import com.github.frapontillo.pulse.crowd.social.profile.ProfilerException;

import java.util.List;

/**
 * @author Francesco Pontillo
 */
public class FacebookProfiler extends IProfiler {
    public static final String PLUGIN_NAME = "profiler-facebook";
    private static FacebookProfilerRunner runner = null;

    @Override public String getName() {
        return PLUGIN_NAME;
    }

    @Override public List<Profile> getProfiles(ProfileParameters parameters)
            throws ProfilerException {
        return getRunnerInstance().getProfiles(parameters);
    }

    private FacebookProfilerRunner getRunnerInstance() {
        if (runner == null) {
            runner = new FacebookProfilerRunner();
        }
        return runner;
    }
}
