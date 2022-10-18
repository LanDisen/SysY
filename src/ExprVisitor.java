interface ExprVisitor {
    public Object visitOptBinaryExpr(OptBinaryExpr expr);
    public Object visitBinaryExpr(BinaryExpr expr);
    public Object visitExpressionExpr(ExpressionExpr expr);
    public Object visitNumberExpr(NumberExpr expr);
    public Object visitNullExpr(NullExpr expr);

}
