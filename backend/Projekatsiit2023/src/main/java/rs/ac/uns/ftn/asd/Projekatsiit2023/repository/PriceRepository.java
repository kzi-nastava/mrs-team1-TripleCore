package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, VehicleType> {
    @Override
    default void delete(Price entity) {
        throw new UnsupportedOperationException("Deleting from PriceList is forbidden");
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("Deleting from PriceList is forbidden");
    }
}
