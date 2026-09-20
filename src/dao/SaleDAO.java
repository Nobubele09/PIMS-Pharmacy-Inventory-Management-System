package dao;

import database.DatabaseConnection;
import models.SaleItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SaleDAO {

    /*
     * Creates a complete sale transaction.
     *
     * Returns:
     *   Sale ID  -> if successful
     *   -1       -> if the sale failed
     */
    public int createSale(
            double totalAmount,
            int userId,
            List<SaleItem> items
    ) {

        String saleSQL =
                "INSERT INTO sales " +
                "(sale_date, total_amount, user_id) " +
                "VALUES (NOW(), ?, ?)";

        String itemSQL =
                "INSERT INTO sale_items " +
                "(sale_id, medicine_id, quantity_sold, price_at_sale) " +
                "VALUES (?, ?, ?, ?)";

        String stockSQL =
                "UPDATE medicines " +
                "SET quantity_in_stock = quantity_in_stock - ? " +
                "WHERE medicine_id = ? " +
                "AND quantity_in_stock >= ?";

        Connection connection = null;

        try {

            connection = DatabaseConnection.getConnection();

            if (connection == null) {
                return -1;
            }

            // Start transaction
            connection.setAutoCommit(false);

            int saleId;

            // ==========================================
            // INSERT SALE
            // ==========================================

            try (
                    PreparedStatement saleStatement =
                            connection.prepareStatement(
                                    saleSQL,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                saleStatement.setDouble(
                        1,
                        totalAmount
                );

                saleStatement.setInt(
                        2,
                        userId
                );

                saleStatement.executeUpdate();

                try (
                        ResultSet generatedKeys =
                                saleStatement.getGeneratedKeys()
                ) {

                    if (!generatedKeys.next()) {

                        connection.rollback();

                        return -1;
                    }

                    saleId =
                            generatedKeys.getInt(1);
                }
            }

            // ==========================================
            // INSERT SALE ITEMS AND UPDATE STOCK
            // ==========================================

            try (
                    PreparedStatement itemStatement =
                            connection.prepareStatement(itemSQL);

                    PreparedStatement stockStatement =
                            connection.prepareStatement(stockSQL)
            ) {

                for (SaleItem item : items) {

                    // ----------------------------------
                    // Insert sale item
                    // ----------------------------------

                    itemStatement.setInt(
                            1,
                            saleId
                    );

                    itemStatement.setInt(
                            2,
                            item.getMedicineId()
                    );

                    itemStatement.setInt(
                            3,
                            item.getQuantitySold()
                    );

                    itemStatement.setDouble(
                            4,
                            item.getPriceAtSale()
                    );

                    itemStatement.executeUpdate();

                    // ----------------------------------
                    // Reduce medicine stock
                    // ----------------------------------

                    stockStatement.setInt(
                            1,
                            item.getQuantitySold()
                    );

                    stockStatement.setInt(
                            2,
                            item.getMedicineId()
                    );

                    stockStatement.setInt(
                            3,
                            item.getQuantitySold()
                    );

                    int updated =
                            stockStatement.executeUpdate();

                    /*
                     * If no medicine row was updated,
                     * the medicine may no longer have
                     * enough stock.
                     */
                    if (updated == 0) {

                        connection.rollback();

                        return -1;
                    }
                }
            }

            // ==========================================
            // COMMIT
            // ==========================================

            connection.commit();

            System.out.println(
                    "Sale created successfully. Sale ID: "
                            + saleId
            );

            return saleId;

        } catch (SQLException e) {

            System.out.println(
                    "Error creating sale."
            );

            e.printStackTrace();

            if (connection != null) {

                try {
                    connection.rollback();

                } catch (SQLException rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            return -1;

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}