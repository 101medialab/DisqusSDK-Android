package com.hkm.dis;

import com.hkm.disqus.api.ApiConfig;

import retrofit.RestAdapter;

/**
 * Created by zJJ on 11/6/2015.
 */
public class DqUtil {
    public static ApiConfig genConfig() {
        ApiConfig conf = new ApiConfig(
                BuildConfig.DISQUS_API_KEY,
                BuildConfig.DISQUS_DEFAULT_ACCESS,
                RestAdapter.LogLevel.BASIC);

        /**
         * let urlRequestLink = "https://disqus.com/api/3.0/threads/set.json?forum=\(forumShortName)&api_key=\(apiKey)&thread=ident:\(postID)%20\(domain)/?p=\(postID)"
         */

        conf
                .setForumName("hypebeast")
                .setApiSecret(BuildConfig.DISQUS_SECRET)
                .setRedirectUri(BuildConfig.DISQUS_REDIRECT_URI);


        return conf;
    }
}
