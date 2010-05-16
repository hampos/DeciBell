package org.kinkydesign.decibell.db.engine;

import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.ArrayList;
import org.kinkydesign.decibell.*;
import org.kinkydesign.decibell.core.ComponentRegistry;
import org.kinkydesign.decibell.db.*;
import org.kinkydesign.decibell.db.interfaces.JTable;
import org.kinkydesign.decibell.db.interfaces.JTableColumn;
import org.kinkydesign.decibell.db.query.Proposition;
import org.kinkydesign.decibell.db.query.SQLQuery;
import org.kinkydesign.decibell.db.util.Pair;

public class SearchEngine<T> {

    private final DeciBell db;
    private final StatementPool pool;
    private final ComponentRegistry registry;

    public SearchEngine(final DeciBell db) {
        this.db = db;
        pool = StatementPool.getPool(db.getDbConnector());
        registry = ComponentRegistry.getRegistry(db.getDbConnector());
    }

    // <editor-fold defaultstate="collapsed" desc="comment">
    public ArrayList<T> search(Component prototype) {
        return null;
    }// </editor-fold>

    private ArrayList<T> doSearchTerminal(Component component) {
        ArrayList<T> resultList = new ArrayList<T>();

        JTable table = registry.get(component.getClass());
        if (!isTerminal(table)) {
            return null; // The component is not terminal!
        }

        Pair<PreparedStatement, SQLQuery> entry = pool.getSearch(table);
        PreparedStatement ps = entry.getKey();
        SQLQuery query = entry.getValue();

        System.out.println("Searching for : \n"+component+"\n" +
                "using the query :\n"+query);

        for (Proposition proposition : query.getPropositions()){
            
        }



        return resultList;
    }

    /**
     * We call terminal those tables that have no foreign keys except perhaps for
     * some self-referencing ones and do not participate in any many-to-many or
     * one-to-many relations.
     * @param component
     */
    private boolean isTerminal(JTable table) {
        if (!table.getRelations().isEmpty()) {
            return false;
        }
        for (JTableColumn tableColumn : table.getForeignKeyColumns()) {
            if (!tableColumn.isSelfReferencing()) {
                return false;
            }
        }
        return true;

    }
}
