using System;
using System.Threading;
using System.Threading.Tasks;
using Xunit;
using WorkOrderManagement.Application.Commands;
using WorkOrderManagement.Domain.Entities;
using WorkOrderManagement.Domain.Enums;
using WorkOrderManagement.Api.Controllers;
using Microsoft.AspNetCore.Mvc;

namespace WorkOrderManagement.Tests;

public class WorkOrderTests
{
    [Fact]
    public void CreateWorkOrder_WithValidData_CreatesSuccessfully()
    {
        // Arrange
        var equipmentId = Guid.NewGuid();
        var priority = Priority.High;
        var description = "Fix the pump";
        var createdBy = "User1";

        // Act
        var workOrder = WorkOrder.Create(equipmentId, priority, description, createdBy);

        // Assert
        Assert.NotNull(workOrder);
        Assert.NotEqual(Guid.Empty, workOrder.Id);
        Assert.Equal(equipmentId, workOrder.EquipmentId);
        Assert.Equal(priority, workOrder.Priority);
        Assert.Equal(description, workOrder.Description);
        Assert.Equal(WorkOrderStatus.Open, workOrder.Status);
    }

    [Fact]
    public void CreateWorkOrder_WithEmptyDescription_ThrowsArgumentException()
    {
        // Arrange
        var equipmentId = Guid.NewGuid();
        var priority = Priority.High;
        var description = "";
        var createdBy = "User1";

        // Act & Assert
        var exception = Assert.Throws<ArgumentException>(() => 
            WorkOrder.Create(equipmentId, priority, description, createdBy));
        Assert.Contains("Description cannot be empty", exception.Message);
    }

    [Fact]
    public void CreateWorkOrder_WithExtremelyLongDescription_ThrowsArgumentException()
    {
        // Arrange
        var equipmentId = Guid.NewGuid();
        var priority = Priority.High;
        var description = new string('A', 2001); // 2001 chars, exceeds limit
        var createdBy = "User1";

        // Act & Assert
        var exception = Assert.Throws<ArgumentException>(() => 
            WorkOrder.Create(equipmentId, priority, description, createdBy));
        Assert.Contains("Description cannot exceed 2000 characters", exception.Message);
    }
}
