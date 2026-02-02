import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Shield, Plane, Wrench, LogOut, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Badge } from '@/components/ui/badge';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { useAuth } from '@/context';
import { useFailures, useGroundResources, useReportFailure } from '@/hooks';
import type { FailureReportResponse, FailureType, FailureUrgency } from '@/types';

const failureTypeOptions: { value: FailureType; label: string }[] = [
    { value: 'ELECTRICAL', label: 'Elektryczna' },
    { value: 'MECHANICAL', label: 'Mechaniczna' },
    { value: 'HYDRAULIC', label: 'Hydrauliczna' },
    { value: 'SOFTWARE', label: 'Systemowa / IT' },
    { value: 'COMMUNICATION', label: 'Komunikacja' },
    { value: 'POWER_OUTAGE', label: 'Brak zasilania' },
    { value: 'WATER_LEAKAGE', label: 'Wycieki / woda' },
    { value: 'OTHER', label: 'Inna' },
];

const urgencyOptions: { value: FailureUrgency; label: string }[] = [
    { value: 'LOW', label: 'Niska' },
    { value: 'NORMAL', label: 'Normalna' },
    { value: 'HIGH', label: 'Wysoka' },
    { value: 'CRITICAL', label: 'Krytyczna' },
];

const statusClassMap: Record<string, string> = {
    AVAILABLE: 'border-emerald-500/40 bg-emerald-500/15 text-emerald-200',
    IN_USE: 'border-blue-500/40 bg-blue-500/15 text-blue-200',
    IN_SERVICE: 'border-amber-500/40 bg-amber-500/15 text-amber-200',
    OUT_OF_ORDER: 'border-rose-500/40 bg-rose-500/15 text-rose-200',
};

export function GroundOpsPage() {
    const { logout, username } = useAuth();
    const navigate = useNavigate();
    const { data: resources = [], isLoading } = useGroundResources();
    const { data: failures = [] } = useFailures();
    const reportFailure = useReportFailure();

    const [search, setSearch] = useState('');
    const [selectedResourceId, setSelectedResourceId] = useState<number | null>(null);
    const [failureType, setFailureType] = useState<FailureType>('MECHANICAL');
    const [urgency, setUrgency] = useState<FailureUrgency>('NORMAL');
    const [location, setLocation] = useState('');
    const [description, setDescription] = useState('');
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [lastReport, setLastReport] = useState<FailureReportResponse | null>(null);

    useEffect(() => {
        if (!selectedResourceId && resources.length > 0) {
            setSelectedResourceId(resources[0].id);
        }
    }, [resources, selectedResourceId]);

    const filteredResources = useMemo(() => {
        const term = search.trim().toLowerCase();
        if (!term) return resources;
        return resources.filter(resource => {
            const haystack = [
                resource.name,
                resource.details,
                resource.resourceType,
                resource.status,
                resource.id?.toString(),
            ]
                .filter(Boolean)
                .join(' ')
                .toLowerCase();
            return haystack.includes(term);
        });
    }, [resources, search]);

    const selectedResource = resources.find(resource => resource.id === selectedResourceId) || null;

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        setSubmitError(null);

        if (!selectedResourceId) {
            setSubmitError('Wybierz zasob, ktorego dotyczy awaria.');
            return;
        }

        if (!location.trim()) {
            setSubmitError('Podaj lokalizacje lub strefe.');
            return;
        }

        if (!description.trim()) {
            setSubmitError('Opis awarii jest wymagany.');
            return;
        }

        reportFailure.mutate(
            {
                resourceId: selectedResourceId,
                failureType,
                urgency,
                location: location.trim(),
                description: description.trim(),
            },
            {
                onSuccess: (response) => {
                    setLastReport(response);
                    setDescription('');
                },
                onError: () => {
                    setSubmitError('Nie udalo sie zglosic awarii. Sprawdz polaczenie z uslugami.');
                },
            }
        );
    };

    const recentFailures = useMemo(() => {
        return [...failures]
            .sort((a, b) => new Date(b.reportedAt).getTime() - new Date(a.reportedAt).getTime())
            .slice(0, 5);
    }, [failures]);

    return (
        <div className="h-screen bg-slate-950 text-slate-100 flex flex-col overflow-hidden">
            <header className="shrink-0 border-b border-slate-800 bg-slate-900">
                <div className="px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-semibold text-slate-100">Airport Management System</h1>
                    <nav className="flex items-center gap-4">
                        <Link
                            to="/security"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Shield className="w-4 h-4" />
                            Security
                        </Link>
                        <Link
                            to="/flights"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Plane className="w-4 h-4" />
                            Flights
                        </Link>
                        <Link
                            to="/ground-ops"
                            className="flex items-center gap-2 px-4 py-2 rounded-md bg-blue-600 text-white"
                        >
                            <Wrench className="w-4 h-4" />
                            Zasoby
                        </Link>
                        <div className="h-6 w-px bg-slate-700" />
                        <div className="flex items-center gap-2 text-slate-400 text-sm">
                            <User className="w-4 h-4" />
                            <span>{username || 'User'}</span>
                        </div>
                        <Button
                            onClick={handleLogout}
                            variant="ghost"
                            size="sm"
                            className="text-slate-400 hover:text-red-400 hover:bg-slate-800"
                        >
                            <LogOut className="w-4 h-4" />
                        </Button>
                    </nav>
                </div>
            </header>

            <main className="flex-1 min-h-0 p-6 grid grid-cols-12 gap-6">
                <div className="col-span-5 flex flex-col gap-6 min-h-0">
                    <Card className="bg-slate-900/60 border-slate-800">
                        <CardHeader>
                            <CardTitle className="text-lg">Zasoby techniczne</CardTitle>
                            <CardDescription className="text-slate-400">
                                Wybierz zasob z listy lub uzyj wyszukiwarki.
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="flex flex-col gap-4">
                            <Input
                                placeholder="Szukaj po nazwie, ID lub statusie..."
                                value={search}
                                onChange={(event) => setSearch(event.target.value)}
                                className="bg-slate-950 border-slate-800 text-slate-100"
                            />
                            <div className="border border-slate-800 rounded-lg overflow-hidden">
                                <ScrollArea className="h-[360px]">
                                    <Table>
                                        <TableHeader>
                                            <TableRow>
                                                <TableHead>Nazwa</TableHead>
                                                <TableHead>Status</TableHead>
                                                <TableHead>Typ</TableHead>
                                            </TableRow>
                                        </TableHeader>
                                        <TableBody>
                                            {filteredResources.map(resource => (
                                                <TableRow
                                                    key={resource.id}
                                                    data-state={resource.id === selectedResourceId ? 'selected' : undefined}
                                                    className="cursor-pointer"
                                                    onClick={() => setSelectedResourceId(resource.id)}
                                                >
                                                    <TableCell>
                                                        <div className="flex flex-col">
                                                            <span className="font-medium text-slate-100">{resource.name}</span>
                                                            {resource.details && (
                                                                <span className="text-xs text-slate-400">{resource.details}</span>
                                                            )}
                                                        </div>
                                                    </TableCell>
                                                    <TableCell>
                                                        <Badge
                                                            variant="outline"
                                                            className={statusClassMap[resource.status] || 'border-slate-700 text-slate-300'}
                                                        >
                                                            {resource.status.replace(/_/g, ' ')}
                                                        </Badge>
                                                    </TableCell>
                                                    <TableCell className="text-slate-300">
                                                        {resource.resourceType.replace(/_/g, ' ')}
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                            {!isLoading && filteredResources.length === 0 && (
                                                <TableRow>
                                                    <TableCell colSpan={3} className="text-center text-slate-400 py-6">
                                                        Brak zasobow spelniajacych kryteria wyszukiwania.
                                                    </TableCell>
                                                </TableRow>
                                            )}
                                        </TableBody>
                                    </Table>
                                </ScrollArea>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="bg-slate-900/60 border-slate-800">
                        <CardHeader>
                            <CardTitle className="text-lg">Ostatnie zgloszenia</CardTitle>
                            <CardDescription className="text-slate-400">
                                Najnowsze awarie zgloszone przez obsluge naziemna.
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="flex flex-col gap-3">
                            {recentFailures.map(failure => (
                                <div
                                    key={failure.id}
                                    className="rounded-lg border border-slate-800 bg-slate-950/40 px-4 py-3"
                                >
                                    <div className="flex items-center justify-between">
                                        <span className="text-sm font-medium text-slate-100">
                                            {failure.resourceName}
                                        </span>
                                        <Badge variant="outline" className="border-slate-700 text-slate-300">
                                            {failure.urgency}
                                        </Badge>
                                    </div>
                                    <div className="text-xs text-slate-400 mt-1">
                                        {failure.location} * {new Date(failure.reportedAt).toLocaleString()}
                                    </div>
                                    {failure.securityIncidentId && (
                                        <div className="text-xs text-blue-300 mt-1">
                                            Incydent techniczny: #{failure.securityIncidentId}
                                        </div>
                                    )}
                                </div>
                            ))}
                            {recentFailures.length === 0 && (
                                <div className="text-sm text-slate-400">Brak zgloszen do wyswietlenia.</div>
                            )}
                        </CardContent>
                    </Card>
                </div>

                <div className="col-span-7">
                    <Card className="bg-slate-900/70 border-slate-800 h-full">
                        <CardHeader>
                            <CardTitle className="text-lg">Zglos awarie sprzetu</CardTitle>
                            <CardDescription className="text-slate-400">
                                Formularz automatycznie utworzy incydent techniczny w systemie bezpieczenstwa.
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <form className="space-y-5" onSubmit={handleSubmit}>
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="text-sm text-slate-300">Zasob</label>
                                        <Select
                                            value={selectedResourceId ? selectedResourceId.toString() : ''}
                                            onValueChange={(value) => setSelectedResourceId(Number(value))}
                                        >
                                            <SelectTrigger className="mt-2 bg-slate-950 border-slate-800 text-slate-100">
                                                <SelectValue placeholder="Wybierz zasob" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                {resources.map(resource => (
                                                    <SelectItem key={resource.id} value={resource.id.toString()}>
                                                        {resource.name}
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
                                        {selectedResource && (
                                            <div className="mt-2 text-xs text-slate-400">
                                                {selectedResource.details || 'Brak dodatkowych informacji'}
                                            </div>
                                        )}
                                    </div>
                                    <div>
                                        <label className="text-sm text-slate-300">Typ awarii</label>
                                        <Select value={failureType} onValueChange={(value) => setFailureType(value as FailureType)}>
                                            <SelectTrigger className="mt-2 bg-slate-950 border-slate-800 text-slate-100">
                                                <SelectValue placeholder="Wybierz typ" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                {failureTypeOptions.map(option => (
                                                    <SelectItem key={option.value} value={option.value}>
                                                        {option.label}
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
                                    </div>
                                    <div>
                                        <label className="text-sm text-slate-300">Pilnosc</label>
                                        <Select value={urgency} onValueChange={(value) => setUrgency(value as FailureUrgency)}>
                                            <SelectTrigger className="mt-2 bg-slate-950 border-slate-800 text-slate-100">
                                                <SelectValue placeholder="Wybierz pilnosc" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                {urgencyOptions.map(option => (
                                                    <SelectItem key={option.value} value={option.value}>
                                                        {option.label}
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
                                    </div>
                                    <div>
                                        <label className="text-sm text-slate-300">Lokalizacja / strefa</label>
                                        <Input
                                            value={location}
                                            onChange={(event) => setLocation(event.target.value)}
                                            placeholder="np. Terminal B - Gate 4"
                                            className="mt-2 bg-slate-950 border-slate-800 text-slate-100"
                                        />
                                    </div>
                                </div>

                                <div>
                                    <label className="text-sm text-slate-300">Opis awarii</label>
                                    <Textarea
                                        value={description}
                                        onChange={(event) => setDescription(event.target.value)}
                                        placeholder="Opisz symptomy, czas wystapienia, wplyw na operacje..."
                                        className="mt-2 min-h-[140px] bg-slate-950 border-slate-800 text-slate-100"
                                    />
                                </div>

                                {submitError && (
                                    <div className="rounded-md border border-rose-900 bg-rose-950/30 px-4 py-2 text-sm text-rose-300">
                                        {submitError}
                                    </div>
                                )}

                                {lastReport && (
                                    <div className="rounded-md border border-emerald-900 bg-emerald-950/20 px-4 py-2 text-sm text-emerald-200">
                                        Zgloszenie #{lastReport.id} zapisane.
                                        {lastReport.securityIncidentId && (
                                            <span className="ml-2 text-blue-300">
                                                Incydent techniczny: #{lastReport.securityIncidentId}
                                            </span>
                                        )}
                                    </div>
                                )}

                                <div className="flex items-center justify-between">
                                    <div className="text-xs text-slate-500">
                                        Dane trafiaja do modulu Bezpieczenstwo jako TECHNICAL_EQUIPMENT_FAILURE.
                                    </div>
                                    <Button
                                        type="submit"
                                        disabled={reportFailure.isPending}
                                        className="bg-blue-600 hover:bg-blue-500"
                                    >
                                        {reportFailure.isPending ? 'Wysylanie...' : 'Zglos awarie'}
                                    </Button>
                                </div>
                            </form>
                        </CardContent>
                    </Card>
                </div>
            </main>
        </div>
    );
}
