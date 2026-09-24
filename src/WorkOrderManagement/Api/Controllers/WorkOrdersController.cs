using System;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WorkOrderManagement.Application.Commands;

namespace WorkOrderManagement.Api.Controllers;

[ApiController]
[Route("api/work-orders")]
public class WorkOrdersController : ControllerBase
{
    private readonly CreateWorkOrderCommandHandler _handler;

    // Dependency Injection used here
    public WorkOrdersController(CreateWorkOrderCommandHandler handler)
    {
        _handler = handler;
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

        var workOrderId = await _handler.Handle(command, cancellationToken);
        
        return Created($"/api/work-orders/{workOrderId}", new { Id = workOrderId });
    }
}

public class CreateWorkOrderRequest
{
    public Guid EquipmentId { get; set; }
    public Domain.Enums.Priority Priority { get; set; }
    public string Description { get; set; }
}
