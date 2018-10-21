This interpreter accepts the following programming language:

A program consists of statements.

Whitespace is ignored except inside constants and variable names.

----------------------------------------------------------------------------
STATEMENTS

Each statement is one of
- while statement
- print statement
- assignment statement

Execution proceeds from one statement to the next, until the end of
the program.

----------------------------------------------------------------------------
A while statement has the following form:

  while ( CONDITION ) { STATEMENTS }

The CONDITION is any expression (see below) and the STATEMENTS is a
sequence of arbitrary statements.  The while statement is executed as
follows:

1. evaluate CONDITION
2. if CONDITION evaluated to zero, go to the statement following the
   while statement
3. execute STATEMENTS
4. go back to 1

----------------------------------------------------------------------------
The print statement has the following form:

  print EXPRESSION ;

The statement causes the value of the EXPRESSION to be written to the
standard output.


----------------------------------------------------------------------------
The assignment statement has the following form:

  VARIABLE = EXPRESSION ;

It causes the VARIABLE to hold the value of EXPRESSION.


----------------------------------------------------------------------------
EXPRESSIONS

Expressions consist of nonnegative integer constants and variable
names, combined with the following: addition (+), subtraction (-),
multiplication (*), division (/), equality (=) and less-than (<).
Parentheses may be used in the usual manner.  Evaluation follows the
usual arithmetic rules with the following exceptions:

- the result of a division is rounded toward zero to the nearest integer
- the value of equality and less-than is 1 (for true) or 0 (for false)

Equality and less-than can be chained so that a = b < c means the same as
a = b and b < c.

Variable names follow the standard Unicode identifier rules.
