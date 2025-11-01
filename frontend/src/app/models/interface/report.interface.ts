export interface ReportInterface{
    id: string | null,
    title: string | null,
    description: string | null,
    categoryId: string | null,
    zoneId: string | null,
    severity: string | null,
    reporterId: string | null,
    state: string | null,
    priority: number | null,
    occurredAt: string | null,
    locationText: string | null,
    geom: {
        type: "Point",
        point: number[]
    }
}