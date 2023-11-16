package node;

import token.Token;

public interface Expression
{
    Token getOPToken();
    ExpType getExpType();

}
