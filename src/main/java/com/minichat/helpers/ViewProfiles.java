package com.minichat.helpers;

/**
 * Created by Alexandr on 15.02.2017.
 */
public class ViewProfiles {
    public interface MessageView {
    }

    public interface MessagePost {
    }

    public interface UserPost {
    }

    public interface UserView {
    }

    public interface MessageViewPost extends MessageView, MessagePost {
    }
}
