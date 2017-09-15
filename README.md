crowd-pulse-social-facebook
===========================

Facebook social extractor implementation for Crowd Pulse.

---------------------------

This module contains a collection of plugins (`extractor-facebook`, `profiler-facebook`, 
`reply-extractor-facebook`, `facebook-profile-grapher`) that need a  `facebook4j.properties` file
in the class loader accessible resources directory.

It must hold the following keys and related values:

- `oauth.appId`, your Facebook application ID
- `oauth.appSecret`, your Facebook application secret
