using System;
using System.Threading;
using System.Threading.Tasks;
using WorkOrderManagement.Domain.Entities;
using WorkOrderManagement.Domain.Enums;

namespace WorkOrderManagement.Application.Commands;

public class CreateWorkOrderCommand
{
    public Guid EquipmentId { get; set; }
    public Priority Priority { get; set; }
    public string Description { get; set; }
    public string CreatedBy { get; set; }
}

public interface IWorkOrderRepository
{
    Task AddAsync(WorkOrder workOrder, CancellationToken cancellationToken = default);
    Task SaveChangesAsync(CancellationToken cancellationToken = default);
}

// Handler
public class CreateWorkOrderCommandHandler
{
    private readonly IWorkOrderRepository _repository;

    // Using Constructor Injection for DI (avoiding code smells)
    public CreateWorkOrderCommandHandler(IWorkOrderRepository repository)
    {
        _repository = repository;
    }

    public async Task<Guid> Handle(CreateWorkOrderCommand request, CancellationToken cancellationToken)
    {
        var workOrder = WorkOrder.Create(request.EquipmentId, request.Priority, request.Description, request.CreatedBy);
        
        await _repository.AddAsync(workOrder, cancellationToken);
        await _repository.SaveChangesAsync(cancellationToken);

        return workOrder.Id;
    }
}
