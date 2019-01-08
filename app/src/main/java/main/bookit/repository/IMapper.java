package main.bookit.repository;

public interface IMapper<From, To> {
    To map(From from);
}
