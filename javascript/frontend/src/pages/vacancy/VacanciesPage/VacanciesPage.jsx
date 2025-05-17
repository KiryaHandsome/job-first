import {useEffect, useState} from 'react';
import {VacancyFilters} from '../../../components/features/vacancies/VacancyFilters/VacancyFilters.jsx';
import {apiCall} from '../../../utils/api.js';
import './VacanciesPage.css';
import {VacancyList} from "../../../components/features/vacancies/VacancyList/VacancyList.jsx";
import {getUserResumes} from "../../../services/ResumeService.js";
import {applyToVacancy} from "../../../services/VacancyService.js";

export function VacanciesPage() {
    const [vacancies, setVacancies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [showResumeModal, setShowResumeModal] = useState(false);
    const [selectedVacancyId, setSelectedVacancyId] = useState(null);
    const [resumes, setResumes] = useState([]);
    const [selectedResume, setSelectedResume] = useState(null);
    const [notification, setNotification] = useState(null);
    const [filters, setFilters] = useState({
        search: null,
        salaryFrom: null,
        currency: null,
        experience: null,
        workType: null,
    });

    useEffect(() => {
        console.log('Current page changed:', currentPage);
        fetchVacancies();
    }, [filters, currentPage]);

    const fetchResumes = async () => {
        try {
            const response = await getUserResumes();

            console.log("resumes: " + JSON.stringify(response))

            setResumes(response || []);

        } catch (error) {
            console.error('Error fetching resumes:', error);
            setNotification({
                type: 'error',
                message: 'Не удалось загрузить резюме'
            });
        }
    };

    const handleApplyClick = async (vacancyId) => {
        console.log("handleApplyClick, vacancy: " + JSON.stringify(vacancyId))
        setSelectedVacancyId(vacancyId);
        await fetchResumes();
        setShowResumeModal(true);
    };

    const handleResumeSelect = (resume) => {
        setSelectedResume(resume);
    };

    const handleSubmitApplication = async () => {
        if (!selectedResume) {
            setNotification({
                type: 'error',
                message: 'Пожалуйста, выберите резюме'
            });
            return;
        }

        try {
            const response = await applyToVacancy(selectedVacancyId, selectedResume.id);

            if (response?.code == null) {
                setNotification({
                    type: 'success',
                    message: 'Вы успешно откликнулись на вакансию'
                });
            } else if (response.code === "vacancy.already_applied") {
                setNotification({
                    type: 'error',
                    message: 'Вы уже откликнулись на эту вакансию'
                });

            } else {
                setNotification({
                    type: 'error',
                    message: 'Произошла ошибка'
                });

            }

            setShowResumeModal(false);
            setSelectedResume(null);
            setSelectedVacancyId(null);
        } catch (error) {
            console.error('Error applying to vacancy:', error);
            setNotification({
                type: 'error',
                message: 'Не удалось отправить отклик'
            });
        }
    };

    const closeModal = () => {
        setShowResumeModal(false);
        setSelectedResume(null);
        setSelectedVacancyId(null);
    };

    const fetchVacancies = async () => {
        try {
            setLoading(true);
            console.log('Fetching vacancies for page:', currentPage);

            const {data, pageInfo} = await apiCall('com.job.vacancy.get_vacancies_with_cursor', {
                filters: filters,
                cursor: {
                    pageNumber: currentPage - 1,
                    pageSize: 12
                }
            });

            console.log('API Response:', {
                data,
                pageInfo,
                currentPage,
            });

            setVacancies(Array.isArray(data) ? data : []);
            console.log("PageInfo.totalPages:" + pageInfo.totalPages)
            setTotalPages(pageInfo.totalPages);
        } catch (err) {
            console.error('Error fetching vacancies:', err);
            setError("Не удалось загрузить вакансии");
            setVacancies([]);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (newFilters) => {
        console.log('Filters changed:', newFilters);
        setFilters(prev => ({...prev, ...newFilters}));
        setCurrentPage(1);
    };

    const handlePageChange = (newPage) => {
        console.log('Page change requested:', newPage);
        setCurrentPage(newPage);
    };

    return (
        <div className="vacancies-page">
            {notification && (
                <div className={`notification ${notification.type}`}>
                    {notification.message}
                    <button onClick={() => setNotification(null)}>×</button>
                </div>
            )}
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
                    onApplyClick={handleApplyClick}
                />
            )}

            {showResumeModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Выберите резюме для отклика</h2>
                        <div className="resumes-list">
                            {resumes.map(resume => (
                                <div
                                    key={resume.id}
                                    className={`resume-item ${selectedResume?.id === resume.id ? 'selected' : ''}`}
                                    onClick={() => handleResumeSelect(resume)}
                                >
                                    <h3>{resume.title || 'Без названия'}</h3>
                                </div>
                            ))}
                        </div>
                        <div className="modal-actions">
                            <button className="cancel-button" onClick={closeModal}>
                                Отмена
                            </button>
                            <button
                                className="submit-button"
                                onClick={handleSubmitApplication}
                                disabled={!selectedResume}
                            >
                                Отправить отклик
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
