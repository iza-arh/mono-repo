export interface Zone {
    id: string | null,
    name: string,
    geom: {
        type: string,
        coordinates: number[][][]
    }
}