interface ExprVisitor {
    public Object visitBinaryExpr(BinaryExpr expr);
    public Object visitExpressionExpr(ExpressionExpr expr);
    public Object visitUnaryExpr(UnaryExpr expr);
    public Object visitVarExpr(VarExpr expr);
    public Object visitAssignExpr(AssignExpr expr);
    public Object visitLogicalExpr(LogicalExpr expr);
    public Object visitNumberExpr(NumberExpr expr);
    public Object visitNullExpr(NullExpr expr);

}
