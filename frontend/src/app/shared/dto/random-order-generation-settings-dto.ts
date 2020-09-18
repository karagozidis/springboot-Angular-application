export class RandomOrderGenerationSettingsDTO {
  totalOrders: number;
  minPrice: number;
  maxPrice: number;
  minQuantity: number;
  maxQuantity: number;
  orderDirections: string[];
  orderTypes: string[];
  minIcebergQuantity: number;
  maxIcebergQuantity: number;
  minIcebergPrice: number;
  maxIcebergPrice: number;
  marketCode: string;

  constructor() {
    this.orderTypes = new Array();
    this.orderDirections = new Array();
  }
}
