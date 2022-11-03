interface StmtVisitor {
    Object visitExpressionStmt(ExpressionStmt stmt);
    Object visitPrintStmt(PrintStmt stmt);
    Object visitBlockStmt(BlockStmt stmt);
    Object visitVarDeclStmt(VarDeclStmt stmt);
    Object visitIfStmt(IfStmt stmt);
    Object visitWhileStmt(WhileStmt stmt);
}
