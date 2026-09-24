using System;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using WorkOrderManagement.Application.Commands;

namespace WorkOrderManagement.Api.Controllers;

[Authorize]
[ApiController]
[Route("api/work-orders")]
public class WorkOrdersController : ControllerBase
{
    private readonly CreateWorkOrderCommandHandler _createHandler;
    private readonly UpdateWorkOrderStatusCommandHandler _updateHandler;

    // Dependency Injection used here
    public WorkOrdersController(CreateWorkOrderCommandHandler createHandler, UpdateWorkOrderStatusCommandHandler updateHandler)
    {
        _createHandler = createHandler;
        _updateHandler = updateHandler;
    }

    [HttpPost]
    public async Task<IActionResult> CreateWorkOrder([FromBody] CreateWorkOrderRequest request, CancellationToken cancellationToken)
    {
        // Ideally mapped by MediatR, but calling handler directly for this vertical slice
        var command = new CreateWorkOrderCommand
        {
            EquipmentId = request.EquipmentId,
            Priority = request.Priority,
            Description = request.Description,
            CreatedBy = User.Identity?.Name ?? "System"
        };

        var workOrderId = await _createHandler.Handle(command, cancellationToken);
        
        return Created($"/api/work-orders/{workOrderId}", new { Id = workOrderId });
    }

    [HttpPatch("{id}/status")]
    public async Task<IActionResult> UpdateStatus(Guid id, [FromBody] UpdateWorkOrderStatusRequest request, CancellationToken cancellationToken)
    {
        var command = new UpdateWorkOrderStatusCommand
        {
            WorkOrderId = id,
            NewStatus = request.NewStatus
        };

        var result = await _updateHandler.Handle(command, cancellationToken);
        
        if (!result) return NotFound();
        return NoContent();
    }
}

public class UpdateWorkOrderStatusRequest
{
    public Domain.Enums.WorkOrderStatus NewStatus { get; set; }
}

public class CreateWorkOrderRequest
{
    public Guid EquipmentId { get; set; }
    public Domain.Enums.Priority Priority { get; set; }
    public string Description { get; set; }
}
