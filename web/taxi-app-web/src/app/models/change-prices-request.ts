import { VehiclePricesDTO } from "./vehicle-prices-dto";

export interface ChangePricesRequest{
    adminId: number;
    prices: VehiclePricesDTO;
}