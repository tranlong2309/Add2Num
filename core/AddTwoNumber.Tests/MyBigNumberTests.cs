using AddTwoNumber.Core;
using Serilog;
using Xunit;
using Xunit.Abstractions;

namespace AddTwoNumber.Tests;

/// <summary>
/// Unit tests for <see cref="MyBigNumber.Sum"/>.
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
    public void Sum_BasicCase_ReturnsCorrectResult()
    {
        // Arrange
        string a = "1234";
        string b = "897";

        // Act
        string result = _sut.Sum(a, b);

        // Assert
        Assert.Equal("2131", result);
    }

    [Fact]
    [Trait("Category", "Carry")]
    public void Sum_CarryPropagation_AllNines_ReturnsCorrectResult()
    {
        // Arrange & Act
        string result = _sut.Sum("999", "1");

        // Assert
        Assert.Equal("1000", result);
    }

    [Fact]
    [Trait("Category", "DifferentLengths")]
    public void Sum_DifferentLengths_SmallPlusLarge()
    {
        // Arrange & Act
        string result = _sut.Sum("1", "999999");

        // Assert
        Assert.Equal("1000000", result);
    }

    [Fact]
    [Trait("Category", "SingleDigit")]
    public void Sum_SingleDigits_WithCarry()
    {
        // Arrange & Act
        string result = _sut.Sum("5", "5");

        // Assert
        Assert.Equal("10", result);
    }

    [Fact]
    [Trait("Category", "LargeNumbers")]
    public void Sum_VeryLargeNumbers_BeyondLongRange()
    {
        // Arrange – numbers larger than ulong.MaxValue to verify string-based algorithm
        string a = "99999999999999999999";
        string b = "1";

        // Act
        string result = _sut.Sum(a, b);

        // Assert
        Assert.Equal("100000000000000000000", result);
    }

    [Fact]
    [Trait("Category", "ZeroInput")]
    public void Sum_OneOperandIsZero_ReturnsOtherOperand()
    {
        // Arrange & Act
        string result = _sut.Sum("0", "123");

        // Assert
        Assert.Equal("123", result);
    }

    [Fact]
    [Trait("Category", "ZeroInput")]
    public void Sum_BothOperandsZero_ReturnsZero()
    {
        // Arrange & Act
        string result = _sut.Sum("0", "0");

        // Assert
        Assert.Equal("0", result);
    }

    [Fact]
    [Trait("Category", "LargeNumbers")]
    public void Sum_LargeEqualNumbers()
    {
        // Arrange & Act
        string result = _sut.Sum("500000000000000000000", "500000000000000000000");

        // Assert
        Assert.Equal("1000000000000000000000", result);
    }

    public void Dispose()
    {
        Log.CloseAndFlush();
    }
}
