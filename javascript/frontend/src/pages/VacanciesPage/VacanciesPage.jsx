import { useState, useEffect } from 'react';
import { VacancyList } from '../../components/features/vacancies/VacancyList/VacancyList';
import { VacancyFilters } from '../../components/features/vacancies/VacancyFilters/VacancyFilters';
import { post } from '../../utils/api';
import './VacanciesPage.css';

export function VacanciesPage() {
    const [vacancies, setVacancies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [filters, setFilters] = useState({
        search: '',
        salaryFrom: '',
        salaryTo: '',
        experience: '',
        employmentType: '',
        remote: false
    });

    useEffect(() => {
        console.log('Current page changed:', currentPage);
        fetchVacancies();
    }, [filters, currentPage]);

    const fetchVacancies = async () => {
        try {
            setLoading(true);
            console.log('Fetching vacancies for page:', currentPage);
            
            const { data, pageInfo } = await post('com.job.vacancy.get_vacancies_with_cursor', {
                cursor: {
                    pageNumber: currentPage - 1,
                    pageSize: 10
                }
            });

            console.log('API Response:', { 
                data, 
                pageInfo,
                currentPage,
                calculatedTotalPages: Math.ceil(pageInfo.total / pageInfo.pageSize)
            });

            setVacancies(Array.isArray(data) ? data : []);
            const newTotalPages = Math.ceil(pageInfo.total / pageInfo.pageSize);
            console.log('Setting total pages:', newTotalPages);
            setTotalPages(newTotalPages);
        } catch (err) {
            console.error('Error fetching vacancies:', err);
            setError(err.message);
            setVacancies([]);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (newFilters) => {
        console.log('Filters changed:', newFilters);
        setFilters(prev => ({ ...prev, ...newFilters }));
        setCurrentPage(1);
    };

    const handlePageChange = (newPage) => {
        console.log('Page change requested:', newPage);
        setCurrentPage(newPage);
    };

    return (
        <div className="vacancies-page">
            <h1>Вакансии</h1>
            <VacancyFilters
                filters={filters}
                onFilterChange={handleFilterChange}
            />
            {loading ? (
                <div className="loading">Загрузка...</div>
            ) : error ? (
                <div className="error">{error}</div>
            ) : (
                <VacancyList
                    vacancies={vacancies}
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={handlePageChange}
                />
            )}
        </div>
    );
}
