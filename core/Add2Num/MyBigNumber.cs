using Serilog;

namespace Add2Num;

/// <summary>
/// Provides big-number arithmetic operations.
/// Numbers are represented as numeric strings (digits only, no sign, no separators).
/// </summary>
public class MyBigNumber
{
    private static readonly ILogger _logger = Log.ForContext<MyBigNumber>();

    /// <summary>
    /// Adds two non-negative integers represented as strings using the elementary
    /// school algorithm (right-to-left, digit by digit).
    /// </summary>
    /// <param name="stn1">First number as a string of digits.</param>
    /// <param name="stn2">Second number as a string of digits.</param>
    /// <returns>The sum as a string of digits.</returns>
    public string sum(string stn1, string stn2)
    {
        _logger.Information("Starting sum: stn1={Stn1}, stn2={Stn2}", stn1, stn2);

        int i = stn1.Length - 1;
        int j = stn2.Length - 1;
        int carry = 0;
        int step = 1;

        // Accumulated result digits (will be reversed at the end)
        var resultDigits = new System.Text.StringBuilder();

        while (i >= 0 || j >= 0 || carry > 0)
        {
            int digit1 = (i >= 0) ? (stn1[i] - '0') : 0;
            int digit2 = (j >= 0) ? (stn2[j] - '0') : 0;

            int total = digit1 + digit2 + carry;
            int currentDigit = total % 10;
            carry = total / 10;

            resultDigits.Append(currentDigit);

            _logger.Information(
                "Step {Step}: digit1={Digit1} + digit2={Digit2} + carry_in={CarryIn} = {Total} " +
                "=> write {CurrentDigit}, carry_out={CarryOut}",
                step, digit1, digit2, (total - digit1 - digit2), total, currentDigit, carry);

            i--;
            j--;
            step++;
        }

        // Reverse the accumulated digits to get the final result
        string result = Reverse(resultDigits.ToString());

        _logger.Information("sum complete: {Stn1} + {Stn2} = {Result}", stn1, stn2, result);

        return result;
    }

    private static string Reverse(string s)
    {
        char[] chars = s.ToCharArray();
        System.Array.Reverse(chars);
        return new string(chars);
    }
}
