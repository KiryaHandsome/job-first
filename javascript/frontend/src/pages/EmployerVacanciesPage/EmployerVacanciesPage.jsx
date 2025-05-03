import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../../hooks/useAuth';
import {apiCall} from '../../utils/api';
import './EmployerVacanciesPage.css';
import {getEmployerVacancies} from "../../services/VacancyService.js";
import {EMPLOYER_ROLE} from "../../constants/Common.jsx";
import Notification from "../../components/common/Notification/Notification.jsx";

const EmployerVacanciesPage = () => {
    const [vacancies, setVacancies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const {getCurrentUser} = useAuth();
    const [notification, setNotification] = useState()

    const fetchVacancies = async () => {
        try {
            setLoading(true);
            const response = await getEmployerVacancies();

            console.log("Employervacancies: " + JSON.stringify(response))

            setVacancies(response);
        } catch (error) {
            setError('Не удалось загрузить вакансии');
            console.error('Error fetching vacancies:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const user = getCurrentUser();

        console.log("Emp.vac.page: " + JSON.stringify(user));

        if (user?.role !== EMPLOYER_ROLE) {
            console.log("Not employer. Redirect to all vacancies");
            navigate('/vacancies');
            return;
        }

        fetchVacancies();
    }, []);

    const handleCreateClick = () => {
        navigate('/create-vacancy');
    };

    const handleEditClick = (id) => {
        navigate(`/vacancies/${id}/edit`);
    };

    const handleDeleteClick = async (id) => {
        if (window.confirm('Вы уверены, что хотите удалить эту вакансию?')) {
            try {
                const response = await apiCall('http://localhost:8080/api/com.job.vacancy.delete_vacancy', {
                    id: id
                });
                if (!response.ok) {
                    throw new Error('Failed to delete vacancy');
                }
                setVacancies(vacancies.filter(v => v.id !== id));
            } catch (error) {
                setError('Не удалось удалить вакансию');
                console.error('Error deleting vacancy:', error);
            }
        }
    };

    const formatSalary = (salary) => {
        if (!salary) return 'По договоренности';

        if (typeof salary === 'object') {
            const {min, max, currency} = salary;
            if (min && max) {
                return `${min.toLocaleString()} - ${max.toLocaleString()} ${currency}`;
            }
            if (min) {
                return `от ${min.toLocaleString()} ${currency}`;
            }
            if (max) {
                return `до ${max.toLocaleString()} ${currency}`;
            }
        }

        return salary;
    };

    if (loading) {
        return <div className="my-vacancies-page loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="my-vacancies-page error">{error}</div>;
    }

    return (
        <div className="my-vacancies-page">
            {notification && (
                <Notification
                    type={notification.type}
                    message={notification.message}
                    onClose={closeNotification}
                />
            )}
            <div className="my-vacancies-header">
                <h1>Мои вакансии</h1>
                <button className="create-vacancy-button" onClick={handleCreateClick}>
                    Создать вакансию
                </button>
            </div>

            {vacancies.length === 0 ? (
                <div className="no-vacancies">
                    <p>У вас пока нет созданных вакансий.</p>
                    <button className="create-vacancy-button" onClick={handleCreateClick}>
                        Создать первую вакансию
                    </button>
                </div>
            ) : (
                <div className="vacancies-grid">
                    {vacancies.map((vacancy) => (
                        <div key={vacancy.id} className="vacancy-card">
                            <div className="vacancy-header">
                                <h2 className="vacancy-title">{vacancy.title}</h2>
                                <div className="vacancy-salary">{formatSalary(vacancy.salary)}</div>
                            </div>

                            <div className="vacancy-info">
                                <div className="info-item">
                                    <span className="label">Локация:</span>
                                    <span className="value">{vacancy.location || 'Не указана'}</span>
                                </div>
                                <div className="info-item">
                                    <span className="label">Тип работы:</span>
                                    <span className="value">
                                        {vacancy.workType === 'REMOTE' ? 'Удаленная работа' :
                                            vacancy.workType === 'OFFICE' ? 'В офисе' :
                                                vacancy.workType === 'HYBRID' ? 'Гибридная работа' : 'Не указан'}
                                    </span>
                                </div>
                                <div className="info-item">
                                    <span className="label">Опыт:</span>
                                    <span className="value">
                                        {vacancy.experienceLevel === 'NO_EXPERIENCE' ? 'Без опыта' :
                                            vacancy.experienceLevel === 'FROM_1_TO_3_YEARS' ? '1-3 года' :
                                                vacancy.experienceLevel === 'FROM_3_TO_6_YEARS' ? '3-6 лет' :
                                                    vacancy.experienceLevel === 'MORE_THAN_6_YEARS' ? 'Более 6 лет' : 'Не указан'}
                                    </span>
                                </div>
                            </div>

                            <div className="vacancy-actions">
                                <button
                                    className="edit-button"
                                    onClick={() => handleEditClick(vacancy.id)}
                                >
                                    Редактировать
                                </button>
                                <button
                                    className="delete-button"
                                    onClick={() => handleDeleteClick(vacancy.id)}
                                >
                                    Удалить
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default EmployerVacanciesPage;
