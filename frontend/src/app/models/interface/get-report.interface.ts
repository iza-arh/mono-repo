export interface GetReport {
    id: string | null,
    title: string | null,
    description: string | null,
    categoryId: {
        id: number | null,
        name: string | null
    },
    zoneId: {
        id: string | null,
        name: string | null
    },
    reporter: {
        name: string | null,
        lastName: string | null
    },
    state: string | null,
    severity: string | null,
    priority: number | null,
    occurredAt: string | null,
    createdAt: string | null,
    updatedAt: string | null,
    geom: {
        type: "Point",
        coordinates: null,
        point: number[]
    },
    locationText: string | null
}