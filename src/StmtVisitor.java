interface StmtVisitor {
    Object visitExpressionStmt(ExpressionStmt stmt);
    Object visitPrintStmt(PrintStmt stmt);
}
