using Add2Num;
using Serilog;
using Xunit;
using Xunit.Abstractions;

namespace Add2Num.Tests;

/// <summary>
/// Unit tests for <see cref="MyBigNumber.sum"/>.
/// Serilog is configured to write to xUnit's test output as well as a log file.
/// </summary>
public class MyBigNumberTests : IDisposable
{
    private readonly MyBigNumber _sut;

    public MyBigNumberTests(ITestOutputHelper output)
    {
        // Configure Serilog for the test session: write to test output + file
        Log.Logger = new LoggerConfiguration()
            .MinimumLevel.Debug()
            .WriteTo.TestOutput(output, Serilog.Events.LogEventLevel.Debug)
            .WriteTo.File("logs/add2num-tests-.log", rollingInterval: RollingInterval.Day)
            .CreateLogger();

        _sut = new MyBigNumber();
    }

    [Fact]
    [Trait("Category", "BasicAddition")]
    public void sum_BasicCase_ReturnsCorrectResult()
    {
        // Arrange
        string a = "1234";
        string b = "897";

        // Act
        string result = _sut.sum(a, b);

        // Assert
        Assert.Equal("2131", result);
    }

    [Fact]
    [Trait("Category", "Carry")]
    public void sum_CarryPropagation_AllNines_ReturnsCorrectResult()
    {
        // Arrange & Act
        string result = _sut.sum("999", "1");

        // Assert
        Assert.Equal("1000", result);
    }

    [Fact]
    [Trait("Category", "DifferentLengths")]
    public void sum_DifferentLengths_SmallPlusLarge()
    {
        // Arrange & Act
        string result = _sut.sum("1", "999999");

        // Assert
        Assert.Equal("1000000", result);
    }

    [Fact]
    [Trait("Category", "SingleDigit")]
    public void sum_SingleDigits_WithCarry()
    {
        // Arrange & Act
        string result = _sut.sum("5", "5");

        // Assert
        Assert.Equal("10", result);
    }

    [Fact]
    [Trait("Category", "LargeNumbers")]
    public void sum_VeryLargeNumbers_BeyondLongRange()
    {
        // Arrange – numbers larger than ulong.MaxValue to verify string-based algorithm
        string a = "99999999999999999999";
        string b = "1";

        // Act
        string result = _sut.sum(a, b);

        // Assert
        Assert.Equal("100000000000000000000", result);
    }

    [Fact]
    [Trait("Category", "ZeroInput")]
    public void sum_OneOperandIsZero_ReturnsOtherOperand()
    {
        // Arrange & Act
        string result = _sut.sum("0", "123");

        // Assert
        Assert.Equal("123", result);
    }

    [Fact]
    [Trait("Category", "ZeroInput")]
    public void sum_BothOperandsZero_ReturnsZero()
    {
        // Arrange & Act
        string result = _sut.sum("0", "0");

        // Assert
        Assert.Equal("0", result);
    }

    [Fact]
    [Trait("Category", "LargeNumbers")]
    public void sum_LargeEqualNumbers()
    {
        // Arrange & Act
        string result = _sut.sum("500000000000000000000", "500000000000000000000");

        // Assert
        Assert.Equal("1000000000000000000000", result);
    }

    public void Dispose()
    {
        Log.CloseAndFlush();
    }
}
