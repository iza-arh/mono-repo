export interface Report{
    id: string | null,
    title: string | null,
    description: string | null,
    categoryId: string | null,
    zoneId: string | null,
    severity: string | null,
    UserId: string | null,
    state: string | null,
    priority: number | null,
    occurredAt: Date | null,
    locationText: string | null
}