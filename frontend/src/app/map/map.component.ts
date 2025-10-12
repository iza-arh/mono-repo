import { Component, ViewChild, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-map',
  standalone: true,
  template: `
    <div class="map-wrapper">
      <input #searchBox class="map-search" placeholder="Buscar lugar..." />
      <div #mapContainer class="map-container" style="height:400px;width:100%;"></div>
    </div>
  `,
  styles: [
    `
    .map-wrapper { width: 100%; max-width: 900px; margin-top: 1.5rem; }
    .map-search { width: 100%; padding: 0.5rem; margin-bottom: 0.5rem; box-sizing: border-box; }
    `
  ]
})
export class MapComponent implements AfterViewInit, OnDestroy {
  @ViewChild('searchBox', { static: false }) searchBox!: ElementRef<HTMLInputElement>;
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef<HTMLDivElement>;

  private map: any = null;
  private autocomplete: any = null;

  ngAfterViewInit(): void {
    const init = () => {
      const google = (window as any).google;
      if (!google || !google.maps) return false;

      // create map
      this.map = new google.maps.Map(this.mapContainer.nativeElement, {
        center: { lat: -34.397, lng: 150.644 },
        zoom: 8,
      });

      // create autocomplete
      this.autocomplete = new google.maps.places.Autocomplete(this.searchBox.nativeElement);
      this.autocomplete.addListener('place_changed', () => {
        const place = this.autocomplete.getPlace();
        if (!place.geometry || !place.geometry.location) return;
        this.map.setCenter(place.geometry.location);
        this.map.setZoom(15);
      });

      return true;
    };

    if (!(window as any).google || !(window as any).google.maps) {
      const interval = setInterval(() => {
        if (init()) clearInterval(interval);
      }, 200);
    } else {
      init();
    }
  }

  ngOnDestroy(): void {
    // cleanup listeners if necessary
    try {
      if (this.autocomplete && this.autocomplete.unbindAll) this.autocomplete.unbindAll();
    } catch (e) {
      // ignore
    }
  }
}
