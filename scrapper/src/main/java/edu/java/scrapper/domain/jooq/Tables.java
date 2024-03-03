/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq;


import edu.java.scrapper.domain.jooq.tables.Chat;
import edu.java.scrapper.domain.jooq.tables.ChatLink;
import edu.java.scrapper.domain.jooq.tables.Link;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in the default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Tables {

    /**
     * The table <code>CHAT</code>.
     */
    public static final Chat CHAT = Chat.CHAT;

    /**
     * The table <code>CHAT_LINK</code>.
     */
    public static final ChatLink CHAT_LINK = ChatLink.CHAT_LINK;

    /**
     * The table <code>LINK</code>.
     */
    public static final Link LINK = Link.LINK;
}
