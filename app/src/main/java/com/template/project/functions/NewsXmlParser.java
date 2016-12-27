package com.template.project.functions;

import android.content.Context;
import android.util.Xml;

import com.template.project.R;
import com.template.project.objects.NewsObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 70061667 on 1/12/2016.
 */

public class NewsXmlParser{
    private static final String ns = null;
    private Context context;

    public NewsXmlParser(Context context){
        this.context = context;
    }

    public List<NewsObject> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<NewsObject> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<NewsObject> news = new ArrayList<NewsObject>();

        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_rss));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the channel tag
            if (name.equals(context.getString(R.string.news_tag_channel))) {
                news = readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return news;
    }

    private List<NewsObject> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<NewsObject> news = new ArrayList<NewsObject>();

        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_channel));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the item tag
            if (name.equals(context.getString(R.string.news_tag_item))) {
                news.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return news;
    }

    // Parses the contents of an item. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private NewsObject readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_item));
        String title = null;
        String pubDate = null;
        String image = null;
        String description = null;
        String content = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(context.getString(R.string.news_tag_title))) {
                title = readTitle(parser);
            } else if (name.equals(context.getString(R.string.news_tag_pubdate))) {
                pubDate = readPubDate(parser);
            } else if (name.equals(context.getString(R.string.news_tag_description))) {
                description = readDescription(parser);
                image = readImage(description);
                description = description.substring(description.indexOf("<br />")+7);
            } else if (name.equals(context.getString(R.string.news_tag_content))) {
                content = readContent(parser);
                content = beutifyContent(content);
            } else {
                skip(parser);
            }
        }
        return new NewsObject(title, pubDate, image, description, content);
    }

    private String beutifyContent(String html){
        String contentImage = readImage(html);
        html = html.substring(html.indexOf("<br />"));

        String imageStartTag = "<img width=\"440\" height=\"240\" src=\"";
        String imageEndTag = "\" class=\"attachment-popular-thumb colorbox-18348 wp-post-image\" alt=\"osteoporosis-main\" style=\"margin:0 15px 15px 0\" />";
        String hdImage = imageStartTag + contentImage + imageEndTag;

        return hdImage + html;
    }

    // Processes title tags in the news.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_title));
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, context.getString(R.string.news_tag_title));
        return title;
    }

    // Processes pubDate tags in the news.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_pubdate));
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, context.getString(R.string.news_tag_pubdate));
        return pubDate;
    }

    // Processes description tags in the news.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_description));
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, context.getString(R.string.news_tag_description));
        return description;
    }

    // Extract image url from description
    private String readImage(String description){
        String image = null;
        Pattern pattern =  Pattern.compile( "(?m)(?s)<img\\s+(.*)src\\s*=\\s*\"([^\"]+)\"(.*)" );
        String imageStr = description.substring(0, description.indexOf("<br />"));

        Matcher matcher = pattern.matcher(imageStr);
        if(matcher.matches()) {
            image = matcher.group(2).replace("-220x120", "");
        }
        return image;
    }

    // Processes pubDate tags in the news.
    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, context.getString(R.string.news_tag_content));
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, context.getString(R.string.news_tag_content));
        return content;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // For extracts the text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
