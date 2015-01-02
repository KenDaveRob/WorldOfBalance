package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import utility.NewsContainer;

public final class NewsDAO {

    private NewsDAO() {
    }

    public static Map<Integer, NewsContainer> getNews() throws SQLException {
        Map<Integer, NewsContainer> newsList = new HashMap<Integer, NewsContainer>();

        String query = "SELECT * FROM `news`";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                NewsContainer news = new NewsContainer(rs.getInt("news_id"));
                news.setText(rs.getString("text"));
                news.setCreateTime(rs.getString("create_time"));

                newsList.put(news.getID(), news);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return newsList;
    }

    public static NewsContainer getNews(int news_id) throws SQLException {
        NewsContainer news = null;

        String query = "SELECT * FROM `news` WHERE `news_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, news_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                news = new NewsContainer(rs.getInt("news_id"));
                news.setText(rs.getString("text"));
                news.setCreateTime(rs.getString("create_time"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return news;
    }

    public static NewsContainer getLatestNews() throws SQLException {
        NewsContainer news = null;

        String query = "SELECT * FROM `news` ORDER BY `create_time` DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                news = new NewsContainer(rs.getInt("news_id"));
                news.setText(rs.getString("text"));
                news.setCreateTime(rs.getString("create_time"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return news;
    }
}
